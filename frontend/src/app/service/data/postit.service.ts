import { API_URL } from '../../app.constants';
import { HttpClient } from '@angular/common/http';
import { EventEmitter, Injectable } from '@angular/core';
import { PostIt } from 'src/app/model/postit.model';

@Injectable({
  providedIn: 'root'
})
export class PostItService {

  postitsChanged = new EventEmitter<PostIt[]>();
  constructor(
    private http: HttpClient
  ) { }

  retrieveAllPostits(username) {
    return this.http.get<PostIt[]>(`${API_URL}/user/${username}/postit`);
  }

  delete(username, id) {
    return this.http.delete(`${API_URL}/user/${username}/postit/${id}`)
      .subscribe(deleteResponse => {
        this.retrieveAllPostits(username).subscribe(retrieveAllPostitResponse => {
          this.postitsChanged.emit(retrieveAllPostitResponse);
        });
      }, errorResponse => {

      });
  }

  retrievePostit(username, id) {
    return this.http.get<PostIt>(`${API_URL}/user/${username}/postit/${id}`);
  }

  update(username, id, postit) {
    return this.http.put<PostIt>(
      `${API_URL}/user/${username}/postit/${id}`
      , postit)
      .subscribe(updateResponse => {
        this.retrieveAllPostits(username).subscribe(retrieveAllPostitsResponse => {
          this.postitsChanged.emit(retrieveAllPostitsResponse);
        });
      }, errorResponse => {

      });
  }

  create(username: string, postit: PostIt) {
    postit.email = username;
    return this.http.post<PostIt>(
      `${API_URL}/user/${username}/postit`
      , postit).subscribe(createPostitResponse => {
        this.retrieveAllPostits(username).subscribe(retrieveAllPostitsResponse => {
          this.postitsChanged.emit(retrieveAllPostitsResponse);
        });
      }, error => {

      });
  }
}
