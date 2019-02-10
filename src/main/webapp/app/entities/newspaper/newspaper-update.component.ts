import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { INewspaper } from 'app/shared/model/newspaper.model';
import { NewspaperService } from './newspaper.service';

@Component({
    selector: 'jhi-newspaper-update',
    templateUrl: './newspaper-update.component.html'
})
export class NewspaperUpdateComponent implements OnInit {
    newspaper: INewspaper;
    isSaving: boolean;

    constructor(protected newspaperService: NewspaperService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ newspaper }) => {
            this.newspaper = newspaper;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.newspaper.id !== undefined) {
            this.subscribeToSaveResponse(this.newspaperService.update(this.newspaper));
        } else {
            this.subscribeToSaveResponse(this.newspaperService.create(this.newspaper));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<INewspaper>>) {
        result.subscribe((res: HttpResponse<INewspaper>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
