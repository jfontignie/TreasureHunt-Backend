import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { INewsPaper } from 'app/shared/model/news-paper.model';
import { NewsPaperService } from './news-paper.service';

@Component({
    selector: 'jhi-news-paper-delete-dialog',
    templateUrl: './news-paper-delete-dialog.component.html'
})
export class NewsPaperDeleteDialogComponent {
    newsPaper: INewsPaper;

    constructor(
        protected newsPaperService: NewsPaperService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.newsPaperService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'newsPaperListModification',
                content: 'Deleted an newsPaper'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-news-paper-delete-popup',
    template: ''
})
export class NewsPaperDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ newsPaper }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(NewsPaperDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.newsPaper = newsPaper;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/news-paper', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/news-paper', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
