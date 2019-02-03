import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IFeed } from 'app/shared/model/feed.model';
import { FeedService } from './feed.service';
import { INewsPaper } from 'app/shared/model/news-paper.model';
import { NewsPaperService } from 'app/entities/news-paper';

@Component({
    selector: 'jhi-feed-update',
    templateUrl: './feed-update.component.html'
})
export class FeedUpdateComponent implements OnInit {
    feed: IFeed;
    isSaving: boolean;

    newspapers: INewsPaper[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected feedService: FeedService,
        protected newsPaperService: NewsPaperService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ feed }) => {
            this.feed = feed;
        });
        this.newsPaperService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<INewsPaper[]>) => mayBeOk.ok),
                map((response: HttpResponse<INewsPaper[]>) => response.body)
            )
            .subscribe((res: INewsPaper[]) => (this.newspapers = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.feed.id !== undefined) {
            this.subscribeToSaveResponse(this.feedService.update(this.feed));
        } else {
            this.subscribeToSaveResponse(this.feedService.create(this.feed));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IFeed>>) {
        result.subscribe((res: HttpResponse<IFeed>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackNewsPaperById(index: number, item: INewsPaper) {
        return item.id;
    }
}
