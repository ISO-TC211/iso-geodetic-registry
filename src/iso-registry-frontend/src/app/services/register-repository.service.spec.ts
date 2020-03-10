import { TestBed } from '@angular/core/testing';

import { RegisterRepositoryService } from './register-repository.service';

describe('RegisterRepositoryService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: RegisterRepositoryService = TestBed.get(RegisterRepositoryService);
    expect(service).toBeTruthy();
  });
});
