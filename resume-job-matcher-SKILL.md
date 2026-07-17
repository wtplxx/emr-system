---
name: resume-job-matcher
description: 基于岗位JD智能匹配优化简历，提供STAR重构、量化呈现、结构优化等8维度优化及专属面试准备；当用户需要简历优化、简历诊断或面试准备时使用
dependency:
  python:
    - PyPDF2==3.0.1
    - python-docx==1.1.0
---

# 岗位简历匹配优化大师

## 任务目标
- 本 Skill 用于：根据目标岗位JD智能匹配优化简历并生成专属面试准备文档
- 能力包含：简历-JD关键词匹配分析、STAR原则重构、量化数据强化、动词升级、结构优化、招聘系统适配、**15道面试问题生成**
- 触发条件：用户提供岗位JD和简历文件或文本，请求简历优化、简历诊断、面试准备

## 前置准备
- 依赖说明：PyPDF2（PDF解析）、python-docx（Word解析）
- 必需输入：目标岗位JD（文本/PDF/Word）+ 个人简历（文本/PDF/Word），缺一不可

## 操作步骤

### 标准流程

**步骤1：内容解析**
- 若用户上传PDF或Word文件，调用脚本解析：
  - JD文件：`python scripts/file_parser.py --file_path <JD文件路径>`
  - 简历文件：`python scripts/file_parser.py --file_path <简历文件路径>`
- 若用户直接粘贴文本，直接使用文本内容
- 异常处理：文件无法解析时提示用户更换文件或粘贴纯文本

**步骤2：生成匹配分析报告（≤600字）**
- 岗位匹配度评估（高/中/低，简述理由）
- JD核心关键词提取（技能、经验、工具、职责）
- 简历关键词命中情况（已命中/缺失）
- 原始简历问题总结（结构、表述、量化、关键词等维度）
- 核心优化方向（3-5个重点）
- 生成HTML报告文件：
  ```bash
  python scripts/html_generator.py --report_type analysis --data '<JSON数据>' --output_path analysis_report.html
  ```
  JSON数据格式：`{"match_score": "高/中/低", "match_reason": "...", "jd_keywords": [...], "hit_keywords": [...], "miss_keywords": [...], "problems": [...], "directions": [...]}`

**步骤3：执行8维度简历优化**
遵循 [references/optimization-rules.md](references/optimization-rules.md) 中的详细规则：
1. 关键词匹配优化：自然补齐缺失关键词，不堆砌
2. STAR原则重构：每条经历按情境-任务-行动-结果梳理
3. 量化数据强化：补充规模、效率、提升、完成情况等数据
4. 动词升级：使用职业化、有说服力的动词，平实专业
5. 胜任力呈现：自然体现执行力、协作、责任心、结果导向
6. 结构逻辑优化：个人信息→求职意向→核心技能→工作经历→项目经历→教育背景，时间倒序
7. 招聘系统适配：纯文本友好，无复杂表格、特殊符号、图片文字
8. 合规优化：职业空窗、工作变动等问题的真实合理表述

**步骤4：生成新旧对比明细**
- 仅展示有实际修改的内容
- 格式：原始内容 / 优化后内容 / 修改说明
- 无修改部分不重复展示
- 生成HTML对比文档文件：
  ```bash
  python scripts/html_generator.py --report_type comparison --data '<JSON数据>' --output_path comparison_detail.html
  ```
  JSON数据格式：`{"comparisons": [{"section": "...", "original": "...", "optimized": "...", "note": "..."}]}`

**步骤5：输出最终优化简历（800-1200字）**
遵循 [references/resume-format-spec.md](references/resume-format-spec.md) 中的标准格式：
- 模块：个人信息、求职意向、核心技能、工作经历、项目经历、教育背景
- 提取简历中的关键信息，构建结构化数据
- 生成HTML简历文件：
  ```bash
  python scripts/html_generator.py --report_type resume --data '<JSON数据>' --output_path optimized_resume.html
  ```
  **JSON数据格式**（必须包含所有字段）：
  ```json
  {
    "name": "张三",
    "contact": "138****0000 | zhangsan@email.com | 北京 | 28岁",
    "target_job": "产品经理",
    "city": "北京",
    "salary": "25-35K",
    "start_time": "1个月内",
    "skills": "产品策划 | 数据分析 | 用户增长",
    "work_experience": [
      {
        "company": "某科技公司",
        "position": "高级产品经理",
        "time": "2021.06 - 至今",
        "achievements": ["负责产品全生命周期管理", "用户留存率提升35%"]
      }
    ],
    "project_experience": [
      {
        "project": "社交APP",
        "role": "项目负责人",
        "time": "2022.03 - 2022.12",
        "achievements": ["DAU突破10万"]
      }
    ],
    "education": {
      "school": "北京大学",
      "major": "计算机科学与技术 · 本科",
      "time": "2015.09 - 2019.06"
    }
  }
  ```
  **智能体职责**：必须从优化后的简历中提取以上字段的具体内容，填充到JSON中，确保HTML简历包含完整信息

**步骤6：提供使用说明**
- 说明优化后简历HTML文件可直接复制到Word/PDF，或使用浏览器打印功能保存

**步骤7：生成面试准备文档（≤1500字）**
- 面试高频问题分类：自我介绍、简历经历深挖、岗位专业问题、行为面试题、情景面试题、压力面试题、反问面试官问题
- **必须生成15道面试题**，每类2-3道，确保覆盖各类面试场景
- 每道问题专属参考答案：严格贴合优化后简历+岗位JD，基于真实经历
- 面试回答技巧&亮点提示
- 生成HTML面试文档文件：
  ```bash
  python scripts/html_generator.py --report_type interview --data '<JSON数据>' --output_path interview_prep.html
  ```
  JSON数据格式：`{"categories": [{"category_name": "...", "questions": [{"question": "...", "answer": "...", "tips": "..."}]}]}`

### 可选分支
- 当仅提供JD：提示"请补充个人简历"
- 当仅提供简历：提示"请补充目标岗位JD"
- 当要求虚构经历/学历/证书：拒绝服务，明确说明只能基于真实经历优化

## 使用示例

- 示例1：
  - 场景/输入：用户上传"产品经理JD.pdf"和个人简历.docx，请求优化简历
  - 预期产出：analysis_report.html + comparison_detail.html + optimized_resume.html + interview_prep.html（15道面试题）
  - 关键要点：先调用脚本解析两个文件，确保内容完整后执行优化流程；关键词匹配自然融入经历描述；STAR原则清晰重构每条经历；调用html_generator.py生成四个HTML文件；**确保生成15道覆盖各类场景的面试题**；面试答案与简历内容严格一致

- 示例2：
  - 场景/输入：用户直接粘贴JD文本和简历文本，请求简历诊断和优化建议
  - 预期产出：analysis_report.html + comparison_detail.html + optimized_resume.html + interview_prep.html（15道面试题）
  - 关键要点：直接使用文本内容，无需文件解析；分析报告突出问题点和优化方向；量化数据强化优先；调用html_generator.py生成四个HTML文件；**按7类问题每类2-3道，共计15道面试题**；面试问题覆盖典型场景

## 资源索引
- 脚本：见 [scripts/file_parser.py](scripts/file_parser.py)（用途：解析PDF和Word文件为纯文本；参数：--file_path 文件路径）
- 脚本：见 [scripts/html_generator.py](scripts/html_generator.py)（用途：生成HTML文档；参数：--report_type analysis/comparison/interview/resume、--data JSON数据、--output_path 输出路径）
- 资产：见 [assets/analysis_report.html](assets/analysis_report.html)（用途：分析报告HTML模板）
- 资产：见 [assets/comparison_detail.html](assets/comparison_detail.html)（用途：对比明细HTML模板）
- 资产：见 [assets/interview_prep.html](assets/interview_prep.html)（用途：面试准备文档HTML模板）
- 资产：见 [assets/resume.html](assets/resume.html)（用途：优化后简历HTML模板）
- 参考：见 [references/resume-format-spec.md](references/resume-format-spec.md)（何时读取：优化简历和输出最终简历时，确保格式规范）
- 参考：见 [references/optimization-rules.md](references/optimization-rules.md)（何时读取：执行8维度优化时，确保优化规则准确执行）

## 注意事项
- 仅在需要解析文件时调用脚本，文本直接粘贴无需调用
- 所有优化基于用户真实经历，严禁虚构、造假、夸大
- 语言平实专业，不使用行业黑话
- 流程固定，严格按7步执行，不跳过、不乱序
- 优先使用脚本处理技术性操作（文件解析和HTML生成），充分发挥智能体能力（分析、创作、优化）
- 控制输出长度，确保完整不截断
- 默认输出HTML文件：分析报告、对比明细、面试文档均输出HTML格式文件
