/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { TalkingNewsBackendTestModule } from '../../../test.module';
import { NewspaperDeleteDialogComponent } from 'app/entities/newspaper/newspaper-delete-dialog.component';
import { NewspaperService } from 'app/entities/newspaper/newspaper.service';

describe('Component Tests', () => {
    describe('Newspaper Management Delete Component', () => {
        let comp: NewspaperDeleteDialogComponent;
        let fixture: ComponentFixture<NewspaperDeleteDialogComponent>;
        let service: NewspaperService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalkingNewsBackendTestModule],
                declarations: [NewspaperDeleteDialogComponent]
            })
                .overrideTemplate(NewspaperDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(NewspaperDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NewspaperService);
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
