import { CrudViewerRowComponent } from './crud-viewer-row.component';
import {Spectator} from "@ngneat/spectator";
import {createComponentFactory} from "@ngneat/spectator/jest";
import {PropertyPipe} from "../../pipes/property.pipe";
import {CrudViewerConfiguration} from "../../types/CrudViewerConfiguration";

describe('CrudViewerRowComponent', () => {
  let spectator: Spectator<CrudViewerRowComponent<TestEntity>>;
  const createComponent = createComponentFactory<CrudViewerRowComponent<TestEntity>>({
    component: CrudViewerRowComponent,
    declarations: [PropertyPipe]
  });

  beforeEach(() => spectator = createComponent());

  it('should create', () => {
    expect(spectator.component).toBeTruthy();
  });

  it('should create with config only', () => {
    spectator.setInput("config", createConfig());
    expect(spectator.component).toBeTruthy();
    expect(spectator.queryAll('[test-id="column"]')).toHaveLength(2);
  });

  it('should render row', () => {
    spectator.setInput("config", createConfig());
    spectator.setInput("entity", new TestEntity(1, "Nice title"));

    const columns = spectator.queryAll('[test-id="column"]');
    expect(columns).toHaveLength(2);
    expect(columns[0]).toContainText("1");
    expect(columns[1]).toContainText("Nice title");
  });

  it('should render row with nested data', () => {
    const config = createConfig();
    config.fields.push({title: "First name", path: "child.firstName"});
    spectator.setInput("config", config);
    spectator.setInput("entity", new TestEntity(1, "Nice title", new TestChildEntity(5, "John")));

    const columns = spectator.queryAll('[test-id="column"]');
    expect(columns).toHaveLength(3);
    expect(columns[2]).toContainText("John");
  });


});

function createConfig(): CrudViewerConfiguration {
  return new CrudViewerConfiguration("", [
    {title: "Id", path: "id"},
    {title: "Name", path: "title"}
  ]);
}

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
    public firstName?: string,
    public lastName?: string
  ) {
  }
}
