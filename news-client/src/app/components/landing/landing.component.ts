import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {NewsService} from "../../services/news.service";
import {TagCount} from "../../interfaces/tag-count";
import {Subscription} from "rxjs";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.css']
})
export class LandingComponent implements OnInit, OnDestroy {
  private newsService = inject(NewsService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  timesInMinutes!: number[];
  tags!: TagCount[];
  getTagsSub?: Subscription;
  selectedTime!: number;

  ngOnInit() {
    this.timesInMinutes = [5, 15, 30, 45, 60];
    this.selectedTime = Number(this.route.snapshot.queryParamMap.get("time"));
    this.getTags(this.selectedTime || 5);
  }

  ngOnDestroy() {
    this.getTagsSub?.unsubscribe();
  }

  getTags(timeInMinutes: number) {
    this.selectedTime = timeInMinutes;
    this.getTagsSub = this.newsService.getTags(timeInMinutes)
      .subscribe({
        next: tags => this.tags = tags,
        error: err => console.error(err.message)
      })
  }

  onTimeSelect(event: any) {
    this.getTags(event.target.value);
    this.router.navigate([], {
      queryParams: { time: event.target.value }
    })
  }
}
