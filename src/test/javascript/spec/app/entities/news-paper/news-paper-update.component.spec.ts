/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { TalkingNewsBackendTestModule } from '../../../test.module';
import { NewsPaperUpdateComponent } from 'app/entities/news-paper/news-paper-update.component';
import { NewsPaperService } from 'app/entities/news-paper/news-paper.service';
import { NewsPaper } from 'app/shared/model/news-paper.model';

describe('Component Tests', () => {
    describe('NewsPaper Management Update Component', () => {
        let comp: NewsPaperUpdateComponent;
        let fixture: ComponentFixture<NewsPaperUpdateComponent>;
        let service: NewsPaperService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalkingNewsBackendTestModule],
                declarations: [NewsPaperUpdateComponent]
            })
                .overrideTemplate(NewsPaperUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(NewsPaperUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NewsPaperService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new NewsPaper(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.newsPaper = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new NewsPaper();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.newsPaper = entity;
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
