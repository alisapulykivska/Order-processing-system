import { Component } from '@angular/core';
import { UserTableComponent } from "../user-table/user-table.component";

class User {
  id: number;
  name: string;
  email: string;
  role: string;

  constructor(id: number, name: string, email: string, role: string) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.role = role;
  }
}

@Component({
  selector: 'app-admin-panel',
  imports: [UserTableComponent],
  templateUrl: './admin-panel.component.html',
  styleUrl: './admin-panel.component.scss'
})
export class AdminPanelComponent {
  /*users : User[] = [
    new User(1, 'Alice', 'alice@gmail.com', 'admin'),
    new User(2, 'Bob', 'bob@gmail.com', 'user'),
    new User(3, 'Charlie', 'charlie@gmail.com' , 'user'),
  ];*/
  
}
