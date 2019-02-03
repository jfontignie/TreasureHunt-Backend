import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { INewsPaper } from 'app/shared/model/news-paper.model';
import { NewsPaperService } from './news-paper.service';

@Component({
    selector: 'jhi-news-paper-update',
    templateUrl: './news-paper-update.component.html'
})
export class NewsPaperUpdateComponent implements OnInit {
    newsPaper: INewsPaper;
    isSaving: boolean;

    constructor(protected newsPaperService: NewsPaperService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ newsPaper }) => {
            this.newsPaper = newsPaper;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.newsPaper.id !== undefined) {
            this.subscribeToSaveResponse(this.newsPaperService.update(this.newsPaper));
        } else {
            this.subscribeToSaveResponse(this.newsPaperService.create(this.newsPaper));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<INewsPaper>>) {
        result.subscribe((res: HttpResponse<INewsPaper>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
