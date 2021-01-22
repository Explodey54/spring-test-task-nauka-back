import {CrudViewerComponent} from './crud-viewer.component';
import {createHostFactory, Spectator, SpectatorOptions} from "@ngneat/spectator";
import {createComponentFactory} from "@ngneat/spectator/jest";
import {CrudApiService} from "../../service/crud-api/crud-api.service";
import {CrudViewerRowComponent} from "../crud-viewer-row/crud-viewer-row.component";
import {MockComponents } from 'ng-mocks';
import { CrudViewerRowHeadComponent } from '../crud-viewer-row-head/crud-viewer-row-head.component';

describe('CrudViewerComponent', () => {

  const componentCreateConfiguration: SpectatorOptions<CrudViewerComponent<TestEntity>> = {
    component: CrudViewerComponent,
    declarations: MockComponents(CrudViewerRowComponent, CrudViewerRowHeadComponent),
    mocks: [CrudApiService],
  };

  describe('component class', () => {
    let spectator: Spectator<CrudViewerComponent<TestEntity>>;
    const createHost = createHostFactory<CrudViewerComponent<TestEntity>>(componentCreateConfiguration);

    it('should create', () => {
      spectator = createHost("<crud-viewer></crud-viewer>");
      expect(spectator.component).toBeTruthy();
    });

    it('should accept config input', () => {
      const config = { 
        baseApi: '', 
        fields: [ {title: 'id', path: 'id'} ]
      };
      spectator = createHost("<crud-viewer></crud-viewer>", {
        hostProps: { config }
      });
      expect(spectator.component).toBeTruthy();
    });

    it('should render rows for every entity', () => {
      const list = [new TestEntity(1, "title1"), new TestEntity(2, "title2"), new TestEntity(3, "title3")];
      spectator = createHost("<crud-viewer [entityList]='list'></crud-viewer>", {
        hostProps: { list }
      });
      expect(spectator.queryAll("crud-viewer-row")).toHaveLength(3);
    });
  });
});

class TestEntity {
  constructor(
    public id: number,
    public title: string,
    public child?: TestChildEntity
  ) {
  }
}

class TestChildEntity {
  constructor(
    public id: number,
    public firstName: string,
    public lastName: string
  ) {
  }
}
