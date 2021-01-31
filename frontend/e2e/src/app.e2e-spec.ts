import { browser, by, element } from 'protractor';
import { AppPage } from './app.po';

describe('workspace-project App', () => {
  let page: AppPage;
  const testUsername = 'tester';
  const testPassword = 'password';

  beforeEach(() => {
    page = new AppPage();
    page.login(testUsername, testPassword, false);
    page.deleteAllExistingNotes();
    page.logout();
    browser.executeScript('window.sessionStorage.clear();');
    browser.executeScript('window.localStorage.clear();');
  });

  it('that the app is protected by a login screen', () => {
    page.navigateTo('/');
    expect(element(by.css('app-login h1')).getText()).toEqual('POST IT');

    expect(browser.getCurrentUrl()).toMatch('/login$');

    page.navigateTo('/aRandomPath');
    expect(element(by.css('app-login h1')).getText()).toEqual('POST IT');
    expect(browser.getCurrentUrl()).toMatch('/login$');

    page.navigateTo('/login');
    expect(element(by.css('app-login h1')).getText()).toEqual('POST IT');
    expect(browser.getCurrentUrl()).toMatch('/login$');
  });

  it('that the page title equals "POST IT"', () => {
    page.navigateTo('/');
    expect(browser.getTitle()).toEqual('POST IT');
  });

  it('that an authenticated user can create, view, update and delete notes', () => {
    page.navigateTo('/');
    page.login(testUsername, testPassword, false);

    expect(browser.getCurrentUrl()).toMatch('/$');
    element.all(by.css('app-login h1')).then(function (items) {
      expect(items.length).toBe(0);
    });

    page.createNote('Post it note 1');
    page.createNote('Post it note 2');

    element.all(by.css('.note-item')).then(function (items) {
      expect(items.length).toBe(2);
    });

    page.addTextToFirstNoteOnPage('. Added text value.');
    page.deleteAllExistingNotes();
  });

  it('that the app does not remember people who choose not to be remembered', () => {
    page.navigateTo('/');
    page.login(testUsername, testPassword, false);

    page.navigateTo('/login');
    expect(element(by.css('app-login h1')).getText()).toEqual('POST IT');
    expect(browser.getCurrentUrl()).toMatch('/login$');
  });

  it('that the rememberMe functionality remembers a user who redirected back to the login page', () => {
    page.navigateTo('/');
    page.login(testUsername, testPassword, true);
    expect(element(by.css('app-menu span')).getText()).toEqual('POST IT');

    page.navigateTo('/login');
    expect(element(by.css('app-menu span')).getText()).toEqual('POST IT');
    expect(browser.getCurrentUrl()).toMatch('/$');
  });
});
