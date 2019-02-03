import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INewsPaper } from 'app/shared/model/news-paper.model';

@Component({
    selector: 'jhi-news-paper-detail',
    templateUrl: './news-paper-detail.component.html'
})
export class NewsPaperDetailComponent implements OnInit {
    newsPaper: INewsPaper;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ newsPaper }) => {
            this.newsPaper = newsPaper;
        });
    }

    previousState() {
        window.history.back();
    }
}
