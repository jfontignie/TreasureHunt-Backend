import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { TalkingNewsBackendSharedModule } from 'app/shared';
import {
    NewsPaperComponent,
    NewsPaperDetailComponent,
    NewsPaperUpdateComponent,
    NewsPaperDeletePopupComponent,
    NewsPaperDeleteDialogComponent,
    newsPaperRoute,
    newsPaperPopupRoute
} from './';

const ENTITY_STATES = [...newsPaperRoute, ...newsPaperPopupRoute];

@NgModule({
    imports: [TalkingNewsBackendSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        NewsPaperComponent,
        NewsPaperDetailComponent,
        NewsPaperUpdateComponent,
        NewsPaperDeleteDialogComponent,
        NewsPaperDeletePopupComponent
    ],
    entryComponents: [NewsPaperComponent, NewsPaperUpdateComponent, NewsPaperDeleteDialogComponent, NewsPaperDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TalkingNewsBackendNewsPaperModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
