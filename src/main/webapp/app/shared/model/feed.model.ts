import { INewsPaper } from 'app/shared/model/news-paper.model';

export interface IFeed {
    id?: number;
    category?: string;
    feedUrl?: string;
    newsPaper?: INewsPaper;
}

export class Feed implements IFeed {
    constructor(public id?: number, public category?: string, public feedUrl?: string, public newsPaper?: INewsPaper) {}
}
