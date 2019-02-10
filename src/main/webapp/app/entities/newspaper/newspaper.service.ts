import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { INewspaper } from 'app/shared/model/newspaper.model';

type EntityResponseType = HttpResponse<INewspaper>;
type EntityArrayResponseType = HttpResponse<INewspaper[]>;

@Injectable({ providedIn: 'root' })
export class NewspaperService {
    public resourceUrl = SERVER_API_URL + 'api/newspapers';

    constructor(protected http: HttpClient) {}

    create(newspaper: INewspaper): Observable<EntityResponseType> {
        return this.http.post<INewspaper>(this.resourceUrl, newspaper, { observe: 'response' });
    }

    update(newspaper: INewspaper): Observable<EntityResponseType> {
        return this.http.put<INewspaper>(this.resourceUrl, newspaper, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<INewspaper>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<INewspaper[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
