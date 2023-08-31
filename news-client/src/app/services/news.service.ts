import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {News} from "../interfaces/news";
import {TagCount} from "../interfaces/tag-count";

@Injectable({
  providedIn: 'root'
})
export class NewsService {
  private http = inject(HttpClient);
  readonly apiUrl = "/api";

  public createNews(news: News): Observable<News> {
    const formData = new FormData();
    formData.set('title', news.title);
    formData.set('image', news.image);
    formData.set('description', news.description);
    if (news.tags) {
      formData.set('tags', news.tags as any);
    }
    return this.http.post<News>(`${this.apiUrl}/news`, formData);
  }

  public getTags(timeInMinutes: number): Observable<TagCount[]> {
    return this.http.get<TagCount[]>(`${this.apiUrl}/tags`, { params: { time: timeInMinutes } });
  }

  public getNewsByTag(tag: string, timeInMinutes: number): Observable<News[]> {
    return this.http.get<News[]>(`${this.apiUrl}/news`, { params: { tag, time: timeInMinutes } });
  }

}
