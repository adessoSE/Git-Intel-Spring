import { TestBed, inject } from '@angular/core/testing';

import { DataPullService } from './data-pull.service';

describe('DataPullService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DataPullService]
    });
  });

  it('should be created', inject([DataPullService], (service: DataPullService) => {
    expect(service).toBeTruthy();
  }));
});
