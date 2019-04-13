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
    public String getContext() {
        return context;
    }
    public void setContext(String context) {
        this.context = context;
    }

    @Field("summary")
    private String summary;
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Field("type")
    private String type;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    @Field("published")
    private String published;
    public String getPublished() {
        return published;
    }
    public void setPublished(String published) {
        this.published = published;
    }

    @Field(child = true)
    private Target target;
    public Target getTarget() {
        return target;
    }
    public void setTarget(Target target) {
        this.target = target;
    }
}
