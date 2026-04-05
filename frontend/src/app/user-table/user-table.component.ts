import { Component } from '@angular/core';
import { NgFor } from '@angular/common';
import { UserService } from './user.service';
import { User } from './user.model';

/*class User {
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
}*/

@Component({
  selector: 'app-user-table',
  imports: [NgFor],
  templateUrl: './user-table.component.html',
  styleUrl: './user-table.component.scss'
})
export class UserTableComponent {
   /*users : User[] = [
    new User(1, 'Alice', 'alice@gmail.com', 'admin'),
    new User(2, 'Bob', 'bob@gmail.com', 'user'),
    new User(3, 'Charlie', 'charlie@gmail.com' , 'user'),
  ];*/

  users: User[] = [];
    constructor(private userService: UserService) {}
    ngOnInit() {
      this.userService.getUsers().subscribe(data => {
        this.users = data;
      });
    }

  // todo: send requests to backend
  details(user: User) {
  }

  edit(user: User) {
  }

  delete(user: User) {
  }
}
