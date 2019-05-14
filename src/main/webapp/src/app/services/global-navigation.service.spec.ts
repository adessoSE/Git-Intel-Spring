import { TestBed, inject } from '@angular/core/testing';

import { GlobalNavigationService } from './global-navigation.service';

describe('GlobalNavigationService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [GlobalNavigationService]
    });
  });

  it('should be created', inject([GlobalNavigationService], (service: GlobalNavigationService) => {
    expect(service).toBeTruthy();
  }));
});
