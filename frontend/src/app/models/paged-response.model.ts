export interface PagedResponse<T> {
  items: T[];
  page: number;
  size: number;
  totalElements: number;
}