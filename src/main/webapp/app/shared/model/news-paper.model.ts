import { IFeed } from 'app/shared/model/feed.model';

export interface INewsPaper {
    id?: number;
    name?: string;
    pattern?: string;
    feeds?: IFeed[];
}

export class NewsPaper implements INewsPaper {
    constructor(public id?: number, public name?: string, public pattern?: string, public feeds?: IFeed[]) {}
}
