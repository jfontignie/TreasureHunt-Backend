import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'news-paper',
                loadChildren: './news-paper/news-paper.module#TalkingNewsBackendNewsPaperModule'
            },
            {
                path: 'feed',
                loadChildren: './feed/feed.module#TalkingNewsBackendFeedModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TalkingNewsBackendEntityModule {}
