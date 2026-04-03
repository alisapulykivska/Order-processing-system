import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AdminPanelComponent } from './admin-panel/admin-panel.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, AdminPanelComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'frontend';
}
