// deno run --allow-ffi --unstable --allow-read --allow-write

import { DB } from "https://deno.land/x/sqlite@v3.7.2/mod.ts";
import { Base64 } from "https://deno.land/x/bb64@1.1.0/mod.ts";

type Row = [
    number,
    string,
    string,
    Uint8Array | undefined,
    string,
    string
]

class Poem {
    static decoder = new TextDecoder();

    id: number;
    title: string;
    poet: string;
    image: string | undefined;
    verses: string;
    datetime: string;

    constructor(params: { id: number, title: string, poet: string, image?: Uint8Array, verses: string, datetime: string}) {
        const { id, title, poet, image, verses, datetime } = params;
        this.id = id;
        this.title = title;
        this.poet = poet;

        if (image) {
            // this.image = btoa(Poem.decoder.decode(image));
            // this.image = image;
            // this.image = btoa(String.fromCharCode.apply(null, image));
            this.image = Base64.fromUint8Array(image).toString();
        }

        this.verses = verses;
        this.datetime = datetime;
    }
}

const db = new DB("suta_poetry.db");

const output: { poems: Array<Poem> } = { poems: [] };

// const queryStr = "SELECT * FROM poems WHERE id=4";
const queryStr = "SELECT * FROM poems";

for (const [id, title, poet, image, verses, datetime] of (db.query(queryStr) as Array<Row>)) {
    const poem = new Poem({ id, title, poet, image, verses, datetime })
    output.poems.push(poem);
    // console.log(image);
}

db.close();

try {
    Deno.writeTextFileSync("poems.json", JSON.stringify(output));

    console.log("Created poems.json");
} catch (err) {
    console.error("Unable to write output:", err);
}
