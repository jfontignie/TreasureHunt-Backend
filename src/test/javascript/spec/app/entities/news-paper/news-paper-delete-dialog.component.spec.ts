/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { TalkingNewsBackendTestModule } from '../../../test.module';
import { NewsPaperDeleteDialogComponent } from 'app/entities/news-paper/news-paper-delete-dialog.component';
import { NewsPaperService } from 'app/entities/news-paper/news-paper.service';

describe('Component Tests', () => {
    describe('NewsPaper Management Delete Component', () => {
        let comp: NewsPaperDeleteDialogComponent;
        let fixture: ComponentFixture<NewsPaperDeleteDialogComponent>;
        let service: NewsPaperService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalkingNewsBackendTestModule],
                declarations: [NewsPaperDeleteDialogComponent]
            })
                .overrideTemplate(NewsPaperDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(NewsPaperDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NewsPaperService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
