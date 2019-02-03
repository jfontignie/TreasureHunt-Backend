import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IFeed } from 'app/shared/model/feed.model';
import { AccountService } from 'app/core';
import { FeedService } from './feed.service';

@Component({
    selector: 'jhi-feed',
    templateUrl: './feed.component.html'
})
export class FeedComponent implements OnInit, OnDestroy {
    feeds: IFeed[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected feedService: FeedService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.feedService
            .query()
            .pipe(
                filter((res: HttpResponse<IFeed[]>) => res.ok),
                map((res: HttpResponse<IFeed[]>) => res.body)
            )
            .subscribe(
                (res: IFeed[]) => {
                    this.feeds = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInFeeds();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IFeed) {
        return item.id;
    }

    registerChangeInFeeds() {
        this.eventSubscriber = this.eventManager.subscribe('feedListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
