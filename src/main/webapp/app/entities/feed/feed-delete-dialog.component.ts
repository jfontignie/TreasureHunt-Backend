import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFeed } from 'app/shared/model/feed.model';
import { FeedService } from './feed.service';

@Component({
    selector: 'jhi-feed-delete-dialog',
    templateUrl: './feed-delete-dialog.component.html'
})
export class FeedDeleteDialogComponent {
    feed: IFeed;

    constructor(protected feedService: FeedService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.feedService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'feedListModification',
                content: 'Deleted an feed'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-feed-delete-popup',
    template: ''
})
export class FeedDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ feed }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(FeedDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.feed = feed;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/feed', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/feed', { outlets: { popup: null } }]);
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
