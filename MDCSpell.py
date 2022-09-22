import torch
import transformers

from torch import nn
from transformers import AutoModel, AutoTokenizer

class CorrectionNetwork(nn.Module):

    def __init__(self):
        super(CorrectionNetwork, self).__init__()
        # BERT分词器
        self.tokenizer = AutoTokenizer.from_pretrained("hfl/chinese-roberta-wwm-ext")
        # BERT
        self.bert = AutoModel.from_pretrained("hfl/chinese-roberta-wwm-ext")
        # BERT的word embedding，本质就是个nn.Embeddings
        self.word_embedding_table = self.bert.get_input_embeddings()
        # 预测层。hidden_size是词向量的大小，len(self.tokenizer)是词典大小
        self.dense_layer = nn.Linear(self.bert.config.hidden_size, len(self.tokenizer))

    def forward(self, inputs, word_embeddings, detect_hidden_states):
        """
        Correction Network的前向传递
        :param inputs: inputs为tokenizer对中文文本的分词结果，
                       里面包含了token对一个的index，attention_mask等
        :param word_embeddings: 使用BERT的word_embedding对token进行embedding后的结果
        :param detect_hidden_states: Detection Network输出hidden state
        :return: Correction Network对个token的预测结果。
        """
        # 1. 使用bert进行前向传递
        bert_outputs = self.bert(token_type_ids=inputs['token_type_ids'],
                                 attention_mask=inputs['attention_mask'],
                                 inputs_embeds=word_embeddings)
        # 2. 将bert的hidden_state和Detection Network的hidden state进行融合。
        hidden_states = bert_outputs['last_hidden_state'] + detect_hidden_states
        # 3. 最终使用全连接层进行token预测
        return self.dense_layer(hidden_states)

    def get_inputs_and_word_embeddings(self, sequences, max_length=128):
        """
        对中文序列进行分词和word embeddings处理
        :param sequences: 中文文本序列。例如: ["鸡你太美", "哎呦，你干嘛！"]
        :param max_length: 文本的最大长度，不足则进行填充，超出进行裁剪。
        :return: tokenizer的输出和word embeddings.
        """
        inputs = self.tokenizer(sequences, padding='max_length', max_length=max_length, return_tensors='pt',
                                truncation=True)
        word_embeddings = self.word_embedding_table(inputs['input_ids'])
        return inputs, word_embeddings

correction_network = CorrectionNetwork()
inputs, word_embeddings = correction_network.get_inputs_and_word_embeddings(["鸡你太美", "哎呦，你干嘛！"])

print(correction_network(inputs, word_embeddings, torch.rand(2, 128, 768)).size())