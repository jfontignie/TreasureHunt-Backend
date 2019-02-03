import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { INewsPaper } from 'app/shared/model/news-paper.model';

type EntityResponseType = HttpResponse<INewsPaper>;
type EntityArrayResponseType = HttpResponse<INewsPaper[]>;

@Injectable({ providedIn: 'root' })
export class NewsPaperService {
    public resourceUrl = SERVER_API_URL + 'api/news-papers';

    constructor(protected http: HttpClient) {}

    create(newsPaper: INewsPaper): Observable<EntityResponseType> {
        return this.http.post<INewsPaper>(this.resourceUrl, newsPaper, { observe: 'response' });
    }

    update(newsPaper: INewsPaper): Observable<EntityResponseType> {
        return this.http.put<INewsPaper>(this.resourceUrl, newsPaper, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<INewsPaper>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<INewsPaper[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
