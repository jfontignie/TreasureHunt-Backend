/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TalkingNewsBackendTestModule } from '../../../test.module';
import { NewsPaperDetailComponent } from 'app/entities/news-paper/news-paper-detail.component';
import { NewsPaper } from 'app/shared/model/news-paper.model';

describe('Component Tests', () => {
    describe('NewsPaper Management Detail Component', () => {
        let comp: NewsPaperDetailComponent;
        let fixture: ComponentFixture<NewsPaperDetailComponent>;
        const route = ({ data: of({ newsPaper: new NewsPaper(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalkingNewsBackendTestModule],
                declarations: [NewsPaperDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(NewsPaperDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(NewsPaperDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.newsPaper).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
