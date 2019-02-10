import { IFeed } from 'app/shared/model/feed.model';

export interface INewspaper {
    id?: number;
    name?: string;
    pattern?: string;
    feeds?: IFeed[];
}

export class Newspaper implements INewspaper {
    constructor(public id?: number, public name?: string, public pattern?: string, public feeds?: IFeed[]) {}
}
