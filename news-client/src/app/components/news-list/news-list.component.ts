import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {NewsService} from "../../services/news.service";
import {News} from "../../interfaces/news";
import {Location} from "@angular/common";

@Component({
  selector: 'app-news-list',
  templateUrl: './news-list.component.html',
  styleUrls: ['./news-list.component.css']
})
export class NewsListComponent implements OnInit, OnDestroy {
  private location = inject(Location);
  private route = inject(ActivatedRoute);
  private newsService = inject(NewsService);
  tag!: string;
  time!: number;
  news!: News[];
  getNewsSub?: Subscription;

  ngOnInit() {
    this.tag = this.route.snapshot.paramMap.get("tag")!;
    this.time = Number(this.route.snapshot.queryParamMap.get("time"));
    this.getNewsSub = this.newsService.getNewsByTag(this.tag, this.time)
      .subscribe({
        next: news => this.news = news,
        error: err => console.error(err.message)
      })
  }

  ngOnDestroy() {
    this.getNewsSub?.unsubscribe();
  }

  goBack() {
    this.location.back();
  }
}
