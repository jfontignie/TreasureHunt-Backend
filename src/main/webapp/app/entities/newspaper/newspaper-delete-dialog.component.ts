import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { INewspaper } from 'app/shared/model/newspaper.model';
import { NewspaperService } from './newspaper.service';

@Component({
    selector: 'jhi-newspaper-delete-dialog',
    templateUrl: './newspaper-delete-dialog.component.html'
})
export class NewspaperDeleteDialogComponent {
    newspaper: INewspaper;

    constructor(
        protected newspaperService: NewspaperService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.newspaperService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'newspaperListModification',
                content: 'Deleted an newspaper'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-newspaper-delete-popup',
    template: ''
})
export class NewspaperDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ newspaper }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(NewspaperDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.newspaper = newspaper;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/newspaper', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/newspaper', { outlets: { popup: null } }]);
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
