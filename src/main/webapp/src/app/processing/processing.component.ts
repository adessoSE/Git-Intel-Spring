import { Component, Input, OnInit } from '@angular/core';
import { ProcessingOrganizationInfo } from '../entities/processingOrganizationInfo';

@Component({
  selector: 'app-processing',
  templateUrl: './processing.component.html',
  styleUrls: ['./processing.component.css']
})
export class ProcessingComponent implements OnInit {

  @Input() processingInformation: ProcessingOrganizationInfo;
  @Input() progressBarPercentage: number = 0;

  initializedProcessingInterval: boolean = false;

  myStyles = {
    width: 0 + "%"
  };

  ngOnChanges() {
    this.myStyles.width = this.progressBarPercentage + "%";
  }

  constructor() { }

  ngOnInit() {
  }

}
