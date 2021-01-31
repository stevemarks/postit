import { SelectMultipleControlValueAccessor } from '@angular/forms';
import { browser, by, element } from 'protractor';

export class AppPage {
  navigateTo(endpoint) {
    return browser.get(endpoint);
  }

  getParagraphText() {
    return element(by.css('app-login h1')).getText();
  }

  login(email: string, password: string, rememberMe: boolean) {
    this.navigateTo('/');
    const emailElement = element(by.id('email'));
    const passwordElement = element(by.id('password'));
    const rememberMeElement = element(by.id('rememberMe'));

    emailElement.clear().then(() => {
      emailElement.sendKeys(email);
    });
    passwordElement.clear().then(() => {
      passwordElement.sendKeys(password);
    });
    rememberMeElement.isSelected().then((v) => {
      if (rememberMe && !v) {
        rememberMeElement.click();
      } else if (!rememberMe && v) {
        rememberMeElement.click();
      }
    });

    const loginButton = element(by.id('login-button'));
    loginButton.click().then(() => { });
  }

  logout() {
    element(by.id('logout-link')).click();
  }

  createNote(text: string) {
    const createPostitInput = element(by.id('create-postit-text'));
    const createPostitSubmit = element(by.id('create-postit-submit'));
    createPostitInput.clear().then(() => { createPostitInput.sendKeys(text); });
    createPostitSubmit.click();
  }

  addTextToFirstNoteOnPage(textToAdd: string) {
    const nc = element.all(by.css('.note-item')).get(0);
    nc.click();
    nc.getAttribute('id').then((id) => {
      element(by.id('note-' + id)).sendKeys(textToAdd);
      element(by.id('edit-note-submit-' + id)).click();

      element(by.id('note-content-text-' + id)).getText().then(t => {
        expect(t).toContain(textToAdd);
      });
    });
  }

  deleteAllExistingNotes() {
    element.all(by.css('.delete-note')).count().then(function (count) {
      for (let i = 0; i < count; i++) {
        const dn = element.all(by.css('.delete-note')).get(0);
        dn.click();
        dn.getAttribute('id').then((id) => {
          element(by.id('delete-note-' + id)).click();
        });
      }
    });
  }
}
