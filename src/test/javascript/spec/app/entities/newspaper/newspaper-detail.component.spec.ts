/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TalkingNewsBackendTestModule } from '../../../test.module';
import { NewspaperDetailComponent } from 'app/entities/newspaper/newspaper-detail.component';
import { Newspaper } from 'app/shared/model/newspaper.model';

describe('Component Tests', () => {
    describe('Newspaper Management Detail Component', () => {
        let comp: NewspaperDetailComponent;
        let fixture: ComponentFixture<NewspaperDetailComponent>;
        const route = ({ data: of({ newspaper: new Newspaper(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalkingNewsBackendTestModule],
                declarations: [NewspaperDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(NewspaperDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(NewspaperDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.newspaper).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
