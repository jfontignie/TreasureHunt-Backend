import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INewspaper } from 'app/shared/model/newspaper.model';

@Component({
    selector: 'jhi-newspaper-detail',
    templateUrl: './newspaper-detail.component.html'
})
export class NewspaperDetailComponent implements OnInit {
    newspaper: INewspaper;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ newspaper }) => {
            this.newspaper = newspaper;
        });
    }

    previousState() {
        window.history.back();
    }
}
