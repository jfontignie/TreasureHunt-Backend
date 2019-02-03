import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IFeed } from 'app/shared/model/feed.model';

type EntityResponseType = HttpResponse<IFeed>;
type EntityArrayResponseType = HttpResponse<IFeed[]>;

@Injectable({ providedIn: 'root' })
export class FeedService {
    public resourceUrl = SERVER_API_URL + 'api/feeds';

    constructor(protected http: HttpClient) {}

    create(feed: IFeed): Observable<EntityResponseType> {
        return this.http.post<IFeed>(this.resourceUrl, feed, { observe: 'response' });
    }

    update(feed: IFeed): Observable<EntityResponseType> {
        return this.http.put<IFeed>(this.resourceUrl, feed, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IFeed>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IFeed[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
