import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Newspaper } from 'app/shared/model/newspaper.model';
import { NewspaperService } from './newspaper.service';
import { NewspaperComponent } from './newspaper.component';
import { NewspaperDetailComponent } from './newspaper-detail.component';
import { NewspaperUpdateComponent } from './newspaper-update.component';
import { NewspaperDeletePopupComponent } from './newspaper-delete-dialog.component';
import { INewspaper } from 'app/shared/model/newspaper.model';

@Injectable({ providedIn: 'root' })
export class NewspaperResolve implements Resolve<INewspaper> {
    constructor(private service: NewspaperService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<INewspaper> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Newspaper>) => response.ok),
                map((newspaper: HttpResponse<Newspaper>) => newspaper.body)
            );
        }
        return of(new Newspaper());
    }
}

export const newspaperRoute: Routes = [
    {
        path: '',
        component: NewspaperComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talkingNewsBackendApp.newspaper.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: NewspaperDetailComponent,
        resolve: {
            newspaper: NewspaperResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talkingNewsBackendApp.newspaper.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: NewspaperUpdateComponent,
        resolve: {
            newspaper: NewspaperResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talkingNewsBackendApp.newspaper.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: NewspaperUpdateComponent,
        resolve: {
            newspaper: NewspaperResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talkingNewsBackendApp.newspaper.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const newspaperPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: NewspaperDeletePopupComponent,
        resolve: {
            newspaper: NewspaperResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talkingNewsBackendApp.newspaper.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
