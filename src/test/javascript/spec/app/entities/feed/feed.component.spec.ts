/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { TalkingNewsBackendTestModule } from '../../../test.module';
import { FeedComponent } from 'app/entities/feed/feed.component';
import { FeedService } from 'app/entities/feed/feed.service';
import { Feed } from 'app/shared/model/feed.model';

describe('Component Tests', () => {
    describe('Feed Management Component', () => {
        let comp: FeedComponent;
        let fixture: ComponentFixture<FeedComponent>;
        let service: FeedService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalkingNewsBackendTestModule],
                declarations: [FeedComponent],
                providers: []
            })
                .overrideTemplate(FeedComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(FeedComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FeedService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Feed(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.feeds[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
