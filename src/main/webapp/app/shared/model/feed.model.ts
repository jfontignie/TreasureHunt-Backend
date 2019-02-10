import { INewspaper } from 'app/shared/model/newspaper.model';

export interface IFeed {
    id?: number;
    category?: string;
    feedUrl?: string;
    newspaper?: INewspaper;
}

export class Feed implements IFeed {
    constructor(public id?: number, public category?: string, public feedUrl?: string, public newspaper?: INewspaper) {}
}
