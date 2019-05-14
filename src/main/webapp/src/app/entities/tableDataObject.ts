export class TableDataObject {
    public date: Date;
    public value: string;

    constructor(key, value) {
        this.date = key;
        this.value = value;
    }
  };