/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package staatsbibliothek.berlin.hsp.indexUpdateService.kafka;

import org.apache.solr.client.solrj.beans.Field;

public class ActivityStream {
  @Field("@context")
  private String context;
  @Field("summary")
  private String summary;
  @Field("id")
  private String id;
  @Field("type")
  private String type;
  @Field("published")
  private String published;
  @Field("target_type")
  private String target_type;
  @Field("target_id")
  private String target_id;
  @Field("target_name")
  private String target_name;
  @Field("object_id")
  private String object_id;

  public String getContext() {
    return context;
  }

  public void setContext(String context) {
    this.context = context;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getPublished() {
    return published;
  }

  public void setPublished(String published) {
    this.published = published;
  }

  public String getTargetType() {
    return target_type;
  }

  public void setTargetType(String type) {
    this.target_type = target_type;
  }

  public String getTargetId() {
    return target_id;
  }

  public void setTargetId(String target_id) {
    this.target_id = target_id;
  }

  public String getTargetName() {
    return target_name;
  }

  public void setTargetName(String target_name) {
    this.target_name = target_name;
  }

  public String getObjectId() {
    return object_id;
  }

  public void setObjectId(String object_id) {
    this.object_id = object_id;
  }
}
