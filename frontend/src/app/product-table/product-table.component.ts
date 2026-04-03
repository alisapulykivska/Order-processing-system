import { Component } from '@angular/core';
import { NgFor } from '@angular/common';

class Product {
  id: number;
  title: string;
  price: number;
  quantity: number;

  constructor(id: number, title: string, price: number, quantity: number) {
    this.id = id;
    this.title = title;
    this.price = price;
    this.quantity = quantity;
  }
}

@Component({
  selector: 'app-product-table',
  imports: [NgFor],
  templateUrl: './product-table.component.html',
  styleUrl: './product-table.component.scss'
})
export class ProductTableComponent {
  products : Product[] = [
    new Product(1, 'Keyboard', 79, 10),
    new Product(2, 'Mouse', 49, 7),
    new Product(3, 'PC', 1999, 4),
  ];

  details(product: Product) {
  }

  edit(product: Product) {
  }

  delete(product: Product) {
  }
}
