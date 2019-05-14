import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-tile',
  templateUrl: './tile.component.html',
  styleUrls: ['./tile.component.css']
})
export class TileComponent implements OnInit {

  @Input() link: string;
  @Input() displayData: number;
  @Input() title: string;
  @Input() faIcon: string;

  constructor() { }

  ngOnInit() {
  }

}
