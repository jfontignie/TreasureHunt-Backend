import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { NewsPaper } from 'app/shared/model/news-paper.model';
import { NewsPaperService } from './news-paper.service';
import { NewsPaperComponent } from './news-paper.component';
import { NewsPaperDetailComponent } from './news-paper-detail.component';
import { NewsPaperUpdateComponent } from './news-paper-update.component';
import { NewsPaperDeletePopupComponent } from './news-paper-delete-dialog.component';
import { INewsPaper } from 'app/shared/model/news-paper.model';

@Injectable({ providedIn: 'root' })
export class NewsPaperResolve implements Resolve<INewsPaper> {
    constructor(private service: NewsPaperService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<INewsPaper> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<NewsPaper>) => response.ok),
                map((newsPaper: HttpResponse<NewsPaper>) => newsPaper.body)
            );
        }
        return of(new NewsPaper());
    }
}

export const newsPaperRoute: Routes = [
    {
        path: '',
        component: NewsPaperComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talkingNewsBackendApp.newsPaper.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: NewsPaperDetailComponent,
        resolve: {
            newsPaper: NewsPaperResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talkingNewsBackendApp.newsPaper.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: NewsPaperUpdateComponent,
        resolve: {
            newsPaper: NewsPaperResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talkingNewsBackendApp.newsPaper.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: NewsPaperUpdateComponent,
        resolve: {
            newsPaper: NewsPaperResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talkingNewsBackendApp.newsPaper.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const newsPaperPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: NewsPaperDeletePopupComponent,
        resolve: {
            newsPaper: NewsPaperResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talkingNewsBackendApp.newsPaper.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
