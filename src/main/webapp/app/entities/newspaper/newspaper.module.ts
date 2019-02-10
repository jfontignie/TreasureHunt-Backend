import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { TalkingNewsBackendSharedModule } from 'app/shared';
import {
    NewspaperComponent,
    NewspaperDetailComponent,
    NewspaperUpdateComponent,
    NewspaperDeletePopupComponent,
    NewspaperDeleteDialogComponent,
    newspaperRoute,
    newspaperPopupRoute
} from './';

const ENTITY_STATES = [...newspaperRoute, ...newspaperPopupRoute];

@NgModule({
    imports: [TalkingNewsBackendSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        NewspaperComponent,
        NewspaperDetailComponent,
        NewspaperUpdateComponent,
        NewspaperDeleteDialogComponent,
        NewspaperDeletePopupComponent
    ],
    entryComponents: [NewspaperComponent, NewspaperUpdateComponent, NewspaperDeleteDialogComponent, NewspaperDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TalkingNewsBackendNewspaperModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
