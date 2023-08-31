import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NewsService} from "../../services/news.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-share',
  templateUrl: './share.component.html',
  styleUrls: ['./share.component.css']
})
export class ShareComponent implements OnInit, OnDestroy {
  private fb = inject(FormBuilder);
  private newsService = inject(NewsService);
  shareForm!: FormGroup;
  imageFile!: File;
  tags: string[] = [];
  createNewsSub?: Subscription;

  ngOnInit() {
    this.shareForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(5)]],
      image: [null, [Validators.required]],
      description: ['', [Validators.required, Validators.minLength(5)]],
    })
  }

  ngOnDestroy() {
    this.createNewsSub?.unsubscribe();
  }

  onFileChange(event: any) {
    this.imageFile = event.target.files[0];
  }

  addTag(tag: string) {
    const tags = tag.trim().split(" ");
    this.tags.push(...tags);
  }

  removeTag(index: number) {
    this.tags.splice(index, 1);
  }

  onSubmit() {
    this.createNewsSub = this.newsService.createNews({
      ...this.shareForm.value,
      image: this.imageFile,
      tags: this.tags.join(',')
    })
      .subscribe({
        next: news => console.log(news),
        error: err => alert(err.message)
      })
  }

  get title() { return this.shareForm.get('title')!; }
  get image() { return this.shareForm.get('image')!; }
  get description() { return this.shareForm.get('description')!; }
}
