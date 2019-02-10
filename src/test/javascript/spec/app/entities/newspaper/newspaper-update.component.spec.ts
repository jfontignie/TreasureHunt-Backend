/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { TalkingNewsBackendTestModule } from '../../../test.module';
import { NewspaperUpdateComponent } from 'app/entities/newspaper/newspaper-update.component';
import { NewspaperService } from 'app/entities/newspaper/newspaper.service';
import { Newspaper } from 'app/shared/model/newspaper.model';

describe('Component Tests', () => {
    describe('Newspaper Management Update Component', () => {
        let comp: NewspaperUpdateComponent;
        let fixture: ComponentFixture<NewspaperUpdateComponent>;
        let service: NewspaperService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalkingNewsBackendTestModule],
                declarations: [NewspaperUpdateComponent]
            })
                .overrideTemplate(NewspaperUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(NewspaperUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NewspaperService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Newspaper(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.newspaper = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Newspaper();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.newspaper = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
