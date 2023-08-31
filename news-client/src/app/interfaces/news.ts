export interface News {
  id?: string;
  postDate?: number;
  title: string;
  description: string;
  image: File;
  tags?: string[];
}
