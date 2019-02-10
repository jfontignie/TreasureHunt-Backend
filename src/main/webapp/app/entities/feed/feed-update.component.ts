import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IFeed } from 'app/shared/model/feed.model';
import { FeedService } from './feed.service';
import { INewspaper } from 'app/shared/model/newspaper.model';
import { NewspaperService } from 'app/entities/newspaper';

@Component({
    selector: 'jhi-feed-update',
    templateUrl: './feed-update.component.html'
})
export class FeedUpdateComponent implements OnInit {
    feed: IFeed;
    isSaving: boolean;

    newspapers: INewspaper[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected feedService: FeedService,
        protected newspaperService: NewspaperService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ feed }) => {
            this.feed = feed;
        });
        this.newspaperService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<INewspaper[]>) => mayBeOk.ok),
                map((response: HttpResponse<INewspaper[]>) => response.body)
            )
            .subscribe((res: INewspaper[]) => (this.newspapers = res), (res: HttpErrorResponse) => this.onError(res.message));
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

    trackNewspaperById(index: number, item: INewspaper) {
        return item.id;
    }
}
